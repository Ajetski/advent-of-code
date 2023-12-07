(ns ^{:doc "Day 7"
      :author "Adam Jeniski"}
 day07 (:require [clojure.string :as string]
                 [core :refer [get-puzzle-input]]))

(def input (get-puzzle-input 7))

;; part 1
(->> input
     (map #(string/split % #" "))
     (map #(update % 1 (fn [x] (Integer/parseInt x))))
     (sort-by
      (juxt (comp (fn hand-type [hand]
                    (let [v (->> hand
                                 frequencies
                                 vals)]
                      (cond (and (some #(= 3 %) v)
                                 (some #(= 2 %) v))
                            7,
                            (= 2 (count (filter #(= 2 %) v)))
                            5
                            :else (* 2 (apply max v))))) first)
            (comp (partial into [])
                  (partial map (->> "23456789TJQKA"
                                    (map-indexed #(vector %2 (inc %1)))
                                    (into {}))) first)))
     (map-indexed (fn [idx [_ v]]
                    (* v (inc idx))))
     (reduce +))

;; part 2
(->> input
     (map #(string/split % #" "))
     (map #(update % 1 (fn [x] (Integer/parseInt x))))
     (sort-by
      (juxt (comp (fn hand-type [hand]
                    (let [w (+ (count (filter #(= % \J) hand)))
                          v (->> hand
                                 (filter #(not= % \J))
                                 frequencies
                                 vals
                                 (sort >)
                                 (into [])
                                 (#(if (empty? %)
                                     (list 5)
                                     (update % 0 + w))))]
                      (cond (and (some #(= 3 %) v)
                                 (some #(= 2 %) v))
                            7,
                            (= 2 (count (filter #(= 2 %) v)))
                            5
                            :else (* 2 (apply max v))))) first)
            (comp (partial into [])
                  (partial map (->> "J23456789TQKA"
                                    (map-indexed #(vector %2 (inc %1)))
                                    (into {}))) first)))
     (map-indexed (fn [idx [_ v]]
                    (* v (inc idx))))
     (reduce +))

