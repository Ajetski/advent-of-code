(ns day08
  (:require
   [core :as c]
   [input-manager :refer [get-input]]
   [clojure.math.combinatorics :as combo]
   [clojure.set :refer [union]]))

(def input (mapv vec (get-input 8)))

(defn get-char [[row col]]
  (get (get input row) col))

(def locs (->> input
               c/get-coords
               (map #(vector (get-char %) %))
               (group-by first)
               (#(dissoc % \.))
               (#(update-vals % (partial mapv second)))))

(defn get-antinode-pos [[x1 y1] [x2 y2] multiplier]
  (vector (+ x1 (* multiplier (- x2 x1)))
          (+ y1 (+ (* multiplier (- y2 y1))))))

(defn get-antinodes [[a b]]
  (filterv #(get-char %) [(get-antinode-pos a b 2) (get-antinode-pos b a 2)]))

;; part 1
(->> locs
     vals
     (map #(->> (combo/combinations % 2) (mapcat get-antinodes) set))
     (reduce union)
     (count))

(defn get-antinodes-2 [[a b]]
  (concat (take-while get-char (map #(get-antinode-pos a b %) (range)))
          (take-while get-char (map #(get-antinode-pos b a %) (range)))))

;; part 2
(->> locs
     vals
     (map #(->> (combo/combinations % 2) (mapcat get-antinodes-2) set))
     (reduce union)
     (count))

