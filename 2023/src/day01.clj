(ns ^{:doc "Day 1: Trebuchet?!"
      :author "Adam Jeniski"}
 day01 (:require [core :refer [get-puzzle-input]]
                 [clojure.string :as string]))

(def input (get-puzzle-input 1))

;; part 1
(->> input
     (map #(Integer/parseInt
            (str (re-find #"\d" %)
                 (re-find #"\d" (string/reverse %)))))
     (reduce +))

(def numeric-value-map {"one"   1
                        "two"   2
                        "three" 3
                        "four"  4
                        "five"  5
                        "six"   6
                        "seven" 7
                        "eight" 8
                        "nine"  9})
(defn numeric-value [s]
  (or (numeric-value-map s) s))

(defn generate-regex-pattern [mapping-fn]
  (->> (keys numeric-value-map)
       (map mapping-fn)
       (#(conj % "\\d"))
       (string/join "|")
       re-pattern))
(def forwards-search-pattern (generate-regex-pattern identity))
(def backwards-search-pattern (generate-regex-pattern string/reverse))

;; part 2
(->> input
     (map #(Integer/parseInt
            (str (numeric-value (re-find forwards-search-pattern %))
                 (numeric-value (string/reverse
                                 (re-find backwards-search-pattern
                                          (string/reverse %)))))))
     (reduce +))

