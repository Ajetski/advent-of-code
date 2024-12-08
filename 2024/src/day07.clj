(ns day07
  (:require
   [core :as c]
   [input-manager :refer [get-input]]
   [clojure.string :as str]))

(def input (->> (get-input 7)
                (map #(str/split % #": "))
                (c/mmap c/split-whitespace)
                (c/mmmapv parse-long)
                (map #(update % 0 first))
                (into {})))

(defn valid-row? [target acc [v & vs]]
  (if v
    (or (valid-row? target (+ acc v) vs)
        (valid-row? target (* acc v) vs))
    (= target acc)))

;; part 1
(->> input
     (pmap #(when (valid-row? (first %) 0 (second %))
              %))
     (filter identity)
     (map first)
     (reduce +))

(defn valid-row-2?
  ([target [v & vs]] (valid-row-2? target v vs))
  ([target acc [v & vs]]
   (if v
     (or (valid-row-2? target (+ acc v) vs)
         (valid-row-2? target (* acc v) vs)
         (valid-row-2? target (parse-long (str acc v)) vs))
     (= target acc))))

;; part 2
(->> input
     (pmap #(when (apply valid-row-2? %)
              %))
     (filter identity)
     (map first)
     (reduce +))

