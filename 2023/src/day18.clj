(ns day18 (:require [clojure.string :as str]
                    [core :refer [apply-each-v comp> get-puzzle-input]]))

(def dir-map {\U [-1 0], \L [0 -1], \R [0 1], \D [1 0]})
(def hex-dir {\0 \R, \1 \D, \2 \L, \3 \U})

(def input (->> (get-puzzle-input 18)
                (map #(str/split % #" "))
                (map (partial apply-each-v
                              (comp> last dir-map)
                              parse-long
                              (juxt (comp> drop-last last hex-dir dir-map)
                                    #(-> (subs % 2 (- (count %) 2))
                                         (Long/parseLong 16)))))))

;; Shoelace / Pick's Algorithm, wtf?!
(defn area [vs]
  (reduce (fn [[pos ans] [[row col] n]]
            (let [pos (+ pos (* col n))]
              [pos (+ ans
                      (* row n pos)
                      (/ n 2))]))
          [0 1]
          vs))

;; part 1
(area (map drop-last input))

;; part 2
(area (map #(% 2) input))
