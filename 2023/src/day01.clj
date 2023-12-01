(ns ^{:doc "Day 1"
      :author "Adam Jeniski"}
 day01 (:require [core :refer [get-puzzle-input]]
                 [clojure.string :as string]))

(def input (get-puzzle-input 1))

(def numeric-value-map {"one" 1
                        "two" 2
                        "three" 3
                        "four" 4
                        "five" 5
                        "six" 6
                        "seven" 7
                        "eight" 8
                        "nine" 9})

(defn numeric-value [s]
  (or (numeric-value-map s) s))

;; part 1
(->> input
     (map (fn [line]
            {:first (re-find #"\d" line)
             :last (re-find #"\d" (string/reverse line))}))
     (map #(str (:first %) (:last %)))
     (map #(Integer/parseInt %))
     (reduce +))

;; part 2
(def forawrd-search-regex (->> (conj (keys numeric-value-map) "\\d")
                               (string/join "|")
                               re-pattern))

(def backwards-search-regex (->> (conj (->> numeric-value-map keys (map string/reverse)) "\\d")
                                 (string/join "|")
                                 re-pattern))

(->> input
     (map (fn [line]
            (let [f (re-find forawrd-search-regex line)
                  l (string/reverse (re-find backwards-search-regex (string/reverse line)))]
              (str (numeric-value f) (numeric-value l)))))
     (map #(Integer/parseInt %))
     (reduce +))

