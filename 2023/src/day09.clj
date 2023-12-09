(ns day09
  (:require [core :refer [get-puzzle-input]]
            [clojure.string :as str]))

(defn parse-line [line]
  (->> (str/split line #" ")
       (mapv #(Long/parseLong %))))

(defn generate-pyramid [nums]
  (loop [pyramid [nums]]
    (let [last-line (peek pyramid)]
      (if (every? zero? last-line)
        pyramid
        (recur (conj pyramid
                     (mapv - (rest last-line) last-line)))))))

(defn solve [reduction]
  (->> (get-puzzle-input 9)
       (mapv parse-line)
       (mapv #(let [pyrimid (generate-pyramid %)]
                (loop [acc 0
                       line-idx (dec (count pyrimid))]
                  (if (< line-idx 0)
                    acc
                    (recur (reduction (get pyrimid line-idx) acc)
                           (dec line-idx))))))
       (reduce +)))

;; part 1
(solve (fn [line num] (+ (peek line) num)))

;; part 2
(solve (fn [line num] (- (first line) num)))

