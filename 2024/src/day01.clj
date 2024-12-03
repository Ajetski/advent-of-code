(ns day01
  (:require
   [input-manager :refer [get-input]]
   [core :as c]))

(def input (->> (get-input 1)
                (map #(first (c/get-match-groups #"(\d+)\s+(\d+)" %)))
                (map #(mapv parse-long %))
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


