(ns day13
  (:require
   [clojure.string :refer [split-lines]]
   [core :refer [get-puzzle-input]]))

(def input (->> (get-puzzle-input 13)
                (partition-by empty?)
                (filter (comp not-empty first))))

(def sample-input (->> (split-lines "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#")
                       (partition-by empty?)
                       (filter (comp not-empty first))))

(defn get-col [m c] (->> m
                         (filter (fn [[[_ col]]]
                                   (= c col)))
                         (map second)))

(defn get-row [m r] (->> m
                         (filter (fn [[[row]]] (= r row)))
                         (map second)))

(defn get-refl-value [m num-rows num-cols]
  (let [rows (range 0 num-rows)
        cols (range 0 num-cols)]
    (apply
     concat
     (filter
      not-empty
      (list (->>
             rows
             (map #(for [r (range (min (inc %) (- (dec num-rows) %)))]
                     [(- % r) (inc (+ % r))]))
             (filter not-empty)
             (map (partial map (fn [[a b]]
                                 (=  (get-row m a)
                                     (get-row m b)))))
             (map-indexed vector)
             (filter (comp (partial every? identity)
                           second))
             (filter second)
             (map #(vector (inc (first %)) :row)))
            (->> cols
                 (map #(for [c (range (min (inc %) (- (dec num-cols) %)))]
                         [(- % c) (inc (+ % c))]))
                 (filter not-empty)
                 (map (partial map (fn [[a b]]
                                     (= (get-col m a)
                                        (get-col m b)))))
                 (map-indexed vector)
                 (filter (comp (partial every? identity)
                               second))
                 (filter second)
                 (map #(vector (inc (first %)) :col))))))))

(defn gen-map [input]
  (->> input
       (map-indexed (fn [row line]
                      (map-indexed (fn [col c] [[row col] c])
                                   line)))
       (apply concat)
       (into (sorted-map))))

;; part 1
(->> input
     (map #(get-refl-value (gen-map %)
                           (count %)
                           (count (first %))))
     (map first)
     (map #(update % 1 (fn [x] (condp = x
                                 :col 1
                                 :row 100))))
     (map (partial apply *))
     (reduce +)
     )

(defn print-board [m num-rows num-cols]
  (println)
  (doseq [row (range num-rows)]
    (doseq [col (range num-cols)]
      (print (m [row col])))
    (println)))

;; part 2
(->> input
     (mapv
      #(let [num-rows (count %)
             num-cols (count (first %))
             m (gen-map %)
             smudge (first (get-refl-value m num-rows num-cols))]
         (->> (for [r (range num-rows)
                    c (range num-cols)]
                (->> (get-refl-value
                      (update m [r c] (fn [c] (if (= c \#) \. \#)))
                      num-rows
                      num-cols)
                     (filter (partial not= smudge))))
              (filter not-empty)
              (apply concat))))
     (map first)
     (map #(update % 1 (fn [x] (condp = x
                                 :col 1
                                 :row 100))))
     (map (partial apply *))
     (reduce +))

