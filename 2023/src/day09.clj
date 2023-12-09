(ns day09 (:require
           [core :refer [comp> get-puzzle-input mapvf reducef split-spaces]]))

(defn generate-pyramid [nums]
  (loop [pyramid [nums]]
    (let [last-line (peek pyramid)]
      (if (every? zero? last-line)
        pyramid
        (recur (conj pyramid (mapv - (rest last-line) last-line)))))))

(defn solve [reduction]
  (reduce + (map (comp> split-spaces (mapvf parse-long) generate-pyramid reverse
                        (reducef reduction 0))
                 (get-puzzle-input 9))))

;; part 1
(solve (fn [num line] (+ (peek line) num)))

;; part 2
(solve (fn [num line] (- (first line) num)))
