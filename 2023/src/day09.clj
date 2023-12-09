(ns day09 (:require [core :refer [get-puzzle-input]]
                    [clojure.string :as str]))

(defn parse-line [line] (->> (str/split line #" ")
                             (mapv #(Long/parseLong %))))

(defn generate-pyramid [nums]
  (loop [pyramid [nums]]
    (let [last-line (peek pyramid)]
      (if (every? zero? last-line)
        pyramid
        (recur (conj pyramid (mapv - (rest last-line) last-line)))))))

(defn solve [reduction] (->> (get-puzzle-input 9)
                             (map (comp reverse generate-pyramid parse-line))
                             (map #(reduce reduction 0 %))
                             (reduce +)))

;; part 1
(solve (fn [num line] (+ (peek line) num)))

;; part 2
(solve (fn [num line] (- (first line) num)))
