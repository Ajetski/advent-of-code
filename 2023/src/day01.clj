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

(def numeric-value-map
  (->> ["one" "two" "three" "four" "five" "six" "seven" "eight" "nine"]
       (map-indexed (fn [idx val] [val (inc idx)]))
       (into {})))
(defn numeric-value [s]
  (or (numeric-value-map s) s))

(defn generate-regex-pattern [mapping-fn]
  (->> (keys numeric-value-map)
       (map mapping-fn)
       (#(conj % "\\d"))
       (string/join "|")
       re-pattern))
(def first-num-pattern (generate-regex-pattern identity))
(def last-num-pattern (generate-regex-pattern string/reverse))

;; part 2
(->> input
     (map #(Integer/parseInt
            (str (numeric-value (re-find first-num-pattern %))
                 (numeric-value (string/reverse
                                 (re-find last-num-pattern
                                          (string/reverse %)))))))
     (reduce +))

