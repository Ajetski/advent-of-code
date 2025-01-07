(ns day18
  (:require
   [clojure.set :refer [union]]
   [clojure.string :as str]
   [core :as c]
   input-manager))

(def N 1024)

(do
  (def input (->> (input-manager/get-input 2024 18)
                  (map c/split-on-comma)
                  (c/n-mapv 2 parse-long)))

  (def walls (->> input
                  (take N)
                  (apply hash-set)))

  (def end [(->> input
                  (take N)
                 (apply max-key first)
                 first)
            (->> input
                  (take N)
                 (apply max-key second)
                 second)]))

(defn in-bounds? [[x y]]
  (and (>= x 0) (<= x (first end))
       (>= y 0) (<= y (second end))))

(defn potential-steps [[x y]]
  [[(inc x) y]
   [(dec x) y]
   [x (inc y)]
   [x (dec y)]])

;; part 1
(loop [n 0
       q #{[0 0]}
       visited #{[0 0]}]
  (when-not (empty? q)
    (if (q end)
      n
      (recur (inc n)
             (into #{} (mapcat #(->> (potential-steps %)
                                     (filter in-bounds?)
                                     (filter (comp not visited))
                                     (filter (comp not walls)))
                               q))
             (union visited q)))))

;; part 2
(->> input
     (map-indexed vector)
     (drop 2933)
     first
     second
     (str/join ","))
