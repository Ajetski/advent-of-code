(ns day09
  (:require
   [core :as c]
   [input-manager :refer [get-input]]))

(defn partition-by-counts [counts coll]
  (loop [[c & cs] counts
         acc []
         coll coll]
    (cond
      (nil? c) acc
      :else (let [[a b] (split-at c coll)]
              (recur cs (conj acc a) b)))))

(def input (->>
            ; (first (get-input 9))
            "2333133121414131402"
            (map str)
            (map parse-long)))

(def smol-nums (->> input (take-nth 2) (into [])))
(def smol-spaces (->> input rest (take-nth 2) (into [])))

;; part 1
(let [forward (map-indexed #(repeat %2 %1) smol-nums)]
  (->> forward reverse flatten
       (partition-by-counts smol-spaces)
       (interleave forward)
       flatten
       (take (reduce + smol-nums))
       (map-indexed *)
       (reduce +)))
