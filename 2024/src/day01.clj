(ns day01
  (:require
   [core :refer [get-input]]))

(def input (->> (get-input 1)
                (map #(re-seq #"(\d+)\s+(\d+)" %))
                (map (comp rest first))
                (map (partial mapv parse-long))
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


