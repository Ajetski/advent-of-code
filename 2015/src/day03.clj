(ns day03
  (:require [input-manager]
            [clojure.set]))

(def input (input-manager/get-input-raw 2015 3))

(defn get-houses [input]
  (->> input
       (reduce (fn [[[x y] visited]
                    el]
                 (let [pos' (condp = el
                              \> [(inc x) y]
                              \< [(dec x) y]
                              \^ [x (inc y)]
                              \v [x (dec y)])]
                   [pos' (conj visited pos')]))
               [[0 0] #{}])
       second))

;; part 1
(count (get-houses input))

;; part 2
(count (clojure.set/union (get-houses (take-nth 2 input))
                          (get-houses (take-nth 2 (rest input)))))

