(ns potemkin.utils
  (:require
    [potemkin.macros :refer [unify-gensyms] :include-macros true])
)

(defmacro condp-case
  "A variant of condp which has case-like syntax for options.  When comparing
   smaller numbers of keywords, this can be faster, sometimes significantly."
  [predicate value & cases]
  (unify-gensyms
    `(let [val## ~value
           pred## ~predicate]
       (cond
         ~@(->> cases
             (partition 2)
             (map
               (fn [[vals expr]]
                 `(~(if (sequential? vals)
                      `(or ~@(map (fn [x] `(pred## val## ~x)) vals))
                      `(pred## val## ~vals))
                   ~expr)))
             (apply concat))
         :else
         ~(if (even? (count cases))
            `(throw (IllegalArgumentException. (str "no matching clause for " (pr-str val##))))
            (last cases))))))

(defmacro doit
  "An iterable-based version of doseq that doesn't emit inline-destroying chunked-seq code."
  [[x it] & body]
  (let [it-sym (gensym "iterable")]
    `(let [~it-sym ~it
           it# (.iterator ~(with-meta it-sym {:tag "Iterable"}))]
       (loop []
         (when (.hasNext it#)
           (let [~x (.next it#)]
            ~@body)
           (recur))))))

(defmacro doary
  "An array-specific version of doseq."
  [[x ary] & body]
  (let [ary-sym (gensym "ary")]
    `(let [~(with-meta ary-sym {:tag "objects"}) ~ary]
       (dotimes [idx# (alength ~ary-sym)]
         (let [~x (aget ~ary-sym idx#)]
           ~@body)))))
