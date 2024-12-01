(ns day01
  (:require
   [input-manager :refer [get-input]]
   [core :refer :all]))

(def input (->> (get-input 1)
                (map (compose #(re-seq #"(\d+)\s+(\d+)" %)
                              first rest ; only get match groups
                              #(mapv parse-long %)))
                (into {})
                ((juxt keys vals))))

;; part 1
(->> input
     (map sort)
     (apply zipmap)
     (map #(abs (apply - %)))
     (reduce +))

;; part 2
(let [[a b] input
      freqs (frequencies b)]
  (->> a
       (map #(* (or (freqs %) 0) %))
       (reduce +)))


