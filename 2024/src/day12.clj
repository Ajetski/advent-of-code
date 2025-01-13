(ns day12
  (:require
   [clojure.set :as s]
   [core :as c]
   [input-manager :refer [get-input]]))

(def input (c/map-by-coords
            (get-input 2024 12)))

(def cardinal-offsets [[0 1] [0 -1] [1 0] [-1 0]])
(defn steps [loc]
  (c/mmap + (repeat loc) cardinal-offsets))
(defn steps-that-meet-perimeter [loc]
  (let [c (input loc)]
    (->> (steps loc)
         (filter (comp (partial not= c) input)))))

(defn get-shape [loc]
  (let [c (input loc)]
    (loop [acc #{loc}
           seen #{}
           [loc & locs] #{loc}]
      (if (nil? loc)
        acc
        (let [new-locs (->> (steps loc)
                            (filter #(= (input %) c))
                            (filter #(not (seen %))))]
          (recur (into acc new-locs)
                 (conj seen loc)
                 (into locs new-locs)))))))

(defn get-perimeter-count [shape]
  (->> shape
       (map (juxt identity steps-that-meet-perimeter))
       (filter #(seq (second %)))
       (map #(count (second %)))
       (reduce +)))

(defn get-ans [f]
  (loop [acc 0
         [loc & to-see]
         (set (keys input))]
    (if (nil? loc)
      acc
      (let [shape (get-shape loc)
            value (f shape)]
        (recur (+ acc (* (count shape) value))
               (s/difference (set to-see) (set shape)))))))

;; part 1
(get-ans get-perimeter-count)

(defn corner-triples [[row col]]
  (let [[top top-right right bottom-right bottom bottom-left left top-left]
        [[(dec row) col]
         [(dec row) (inc col)]
         [row (inc col)]
         [(inc row) (inc col)]
         [(inc row) col]
         [(inc row) (dec col)]
         [row (dec col)]
         [(dec row) (dec col)]]]
    [[left top-left top]
     [top top-right right]
     [right bottom-right bottom]
     [bottom bottom-left left]]))

(defn corner-count [pos shape]
  (->> pos
       corner-triples
       (c/mmap #(contains? shape %))
       (filter #{[true false true] ;;inner
                 [false true false] ;;outer
                 [false false false]}) ;;outer
       count))

(defn get-num-corners [shape]
  (reduce + (map #(corner-count % shape)
                 shape)))

;; part 2
(get-ans get-num-corners)
