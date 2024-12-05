(ns day05
  (:require [input-manager :refer [get-input]]
            [core :as c]
            [clojure.string :as str]))

(let [[a b] (->> (get-input 5)
                 (partition-by empty?)
                 (take-nth 2))]
  (def orderings (->> (map #(str/split % #"\|") a)
                      (map #(mapv parse-long %))
                      (group-by first)
                      (map #(update % 1 (comp set (partial map second))))
                      (into {})))
  (def pages (->> (map #(str/split % #",") b)
                  (map #(mapv parse-long %)))))

(defn get-middle [v]
  (get v (int (/ (count v) 2))))

(defn ordered? [coll]
  (reduce (fn [acc p]
            (if p
              (if (some acc (orderings p))
                (reduced false)
                (conj acc p))
              (reduced true)))
          #{}
          coll))

(defn order [coll]
  (reduce (fn [acc p]
            (if p
              (if-let [idxs (->> (orderings p)
                                 (filter #(contains? (set acc) %))
                                 (map #(.indexOf acc %))
                                 (filter #(not= % -1))
                                 not-empty)]
                (let [m (apply min idxs)]
                  (into [] (concat (take m acc)
                                   (list p)
                                   (drop m acc))))
                (conj acc p))
              (reduced acc)))
          []
          coll))

;; part 1
(->> pages
     (filter ordered?)
     (map get-middle)
     (reduce +))

;; part 2
(->> pages
     (filter #(not (ordered? %)))
     (map order)
     (map get-middle)
     (reduce +))


