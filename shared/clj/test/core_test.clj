(ns core-test
  (:require [core :as c]
            [clojure.test :refer :all :as t]))

(comment
  (use 'clojure.repl)

  #_{:clj-kondo/ignore [:unresolved-symbol]}
  (dir c)

  (run-tests)

  ;
  )

(deftest math-test
  (testing "math utils"
    (is (= 1 1))))

(deftest utils-test
  (testing "general utils"))

(deftest coll-lib-test
  (testing "alter coll utils"))
