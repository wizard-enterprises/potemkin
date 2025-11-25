(ns potemkin.walk)

;; adapted from clojure.walk, but preserves metadata

#?(:clj  (defn- is-map-entry? [x]
           (instance? clojure.lang.IMapEntry x))
   :cljs (defn- is-map-entry? [x]
           (satisfies? cljs.core/IMapEntry x)))

#?(:clj  (defn- is-record? [x]
           (instance? clojure.lang.IRecord x))
   :cljs (defn- is-record? [x]
           (satisfies? cljs.core/IRecord x)))


(defn walk
  "Like `clojure.walk/walk`, but preserves metadata."
  [inner outer form]
  (let [x (cond
            (list? form) (outer (apply list (map inner form)))
            (is-map-entry? form) (outer (vec (map inner form)))
            (seq? form) (outer (doall (map inner form)))
            (is-record? form) (outer (reduce #(conj %1 (inner %2)) form form))
            (coll? form) (outer (into (empty form) (map inner form)))
            :else (outer form))]
    (cond-> x
      (some? (meta x))
      (with-meta (merge (meta form) (meta x))))))

(defn postwalk
  "Like `clojure.walk/postwalk`, but preserves metadata."
  [f form]
  (walk (partial postwalk f) f form))

(defn prewalk
  "Like `clojure.walk/prewalk`, but preserves metadata."
  [f form]
  (walk (partial prewalk f) identity (f form)))
