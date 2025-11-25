(ns potemkin.utils-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs [cljs.test :refer :all :include-macros true])
   [potemkin.utils :refer :all :include-macros true]
   [criterium.core :as c]))

(deftest test-condp-case
  (let [f #(condp-case identical? %
             (:abc :def) :foo
             :xyz :bar)]
    (is (= :foo (f :abc) (f :def)))
    (is (= :bar (f :xyz)))))
