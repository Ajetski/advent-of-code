(ns day02
  (:require
   [input-manager :refer [get-input]]
   [core :as c]))

(def input (->> (get-input 2)
                (map c/split-whitespace)
                (map #(mapv parse-long %))))

(defn get-diffs [coll]
  (->> (map vector coll (rest coll))
       (mapv #(apply - %))))

(defn small-diffs? [diffs]
  (or (every? #(<= 1 % 3) diffs)
      (every? #(<= -3 % -1) diffs)))

(defn remove-at-idx [v idx]
  (concat (subvec v 0 idx) (subvec v (inc idx))))

;; part 1
(->> input
     (mapv get-diffs)
     (filterv small-diffs?)
     count)

;; part 2
(->> input
     (map (fn [coll]
            (map #(remove-at-idx coll %)
                 (range (count coll)))))
     (map #(map get-diffs %))
     (filter #(some small-diffs? %))
     count)

