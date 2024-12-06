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
  (reduce (fn [acc el]
            (if (some acc (orderings el))
              (reduced false)
              (conj acc el)))
          #{} coll))

(defn order [coll]
  (reduce (fn [acc el]
            (if-let [idxs (->> (orderings el)
                               (filter #(contains? (set acc) %))
                               (map #(.indexOf acc %))
                               (filter #(not= % -1))
                               not-empty)]
              (into [] (c/insert-at-idx acc (apply min idxs) el))
              (conj acc el)))
          [] coll))

;; part 1
(->> pages
     (filter ordered?)
     (map get-middle)
     (reduce +))

;; part 2
(->> pages
     (filter #(not (ordered? %)))
     (map (comp get-middle order))
     (reduce +))

