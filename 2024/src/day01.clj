(ns day01
  (:require input-manager))

;; part 1
(defn every-other [coll]
  (take-nth 2 coll))

(def input (->> (input-manager/get-input-raw 2024 1)
                (re-seq #"\d+")
                (map parse-long)
                ((juxt every-other (comp every-other rest)))))

(->> input
     (map sort)
     (apply map (comp abs -))
     (apply +))

;; part 2
(let [[left-col right-col] input
      freqs (frequencies right-col)]
  (->> left-col
       (map #(* (or (freqs %) 0) %))
       (reduce +)))

