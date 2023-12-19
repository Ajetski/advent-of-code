(ns day18 (:require [core :refer [get-puzzle-input]]
                    [clojure.string :as str]))

(def dir-map {\U [0 -1] \L [-1 0] \R [1 0] \D [0 1]
              \3 [0 -1] \2 [-1 0] \0 [1 0] \1 [0 1]})

(def input
  (->> (get-puzzle-input 18)
       (map #(let [[dir n hex-str] (str/split % #" ")]
               [(dir-map (last dir))
                (parse-long n)
                [(dir-map (last (drop-last hex-str)))
                 (Long/parseLong (subs hex-str 2 (- (count hex-str) 2))
                                 16)]]))))

;; Shoelace / Pick's Algorithm, wtf?!
(defn area [vs]
  (reduce (fn [[pos ans]
               [[x y] n]]
            (let [pos' (+ pos
                          (* x n))
                  ans' (+ ans
                          (* y n pos')
                          (/ n 2))]
              [pos' ans']))
          [0 1]
          vs))

;; part 1
(area (map drop-last input))

;; part 2
(area (map #(% 2) input))
