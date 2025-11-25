(ns potemkin.macros-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs [cljs.test :refer :all :include-macros true])
   [potemkin.macros :refer :all :include-macros true]))

(defn simple-unify-form []
  (unify-gensyms
    `(let [cnt## (atom 0)]
       ~@(map
           (fn [_] `(swap! cnt## inc))
           (range 100))
       cnt##)))

(deftest test-unify-form
  (is (= 100 @(eval (simple-unify-form)))))
