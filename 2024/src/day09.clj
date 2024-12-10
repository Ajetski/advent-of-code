(ns day09
  (:require
   [core :as c]
   [input-manager :refer [get-input]]))

(def input (->> (first (get-input 9))
                (map str)
                (map parse-long)))

(def smol-nums (->> input (take-nth 2) (into [])))
(def smol-spaces (->> input rest (take-nth 2) (into [])))

;; part 1
(let [forward (map-indexed #(repeat %2 %1) smol-nums)]
  (->> forward reverse flatten
       (c/partition-by-counts smol-spaces)
       (interleave forward)
       flatten
       (take (reduce + smol-nums))
       (map-indexed *)
       (reduce +)))
