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

(defn get-refl-value [m num-rows num-cols & [discriminator]]
  (let [rows (range 0 num-rows)
        cols (range 0 num-cols)]
    (when discriminator
      (println discriminator))
    (or (->> cols
             (map #(for [c (range (min (inc %) (- (dec num-cols) %)))]
                     [(- % c) (inc (+ % c))]))
             (filter not-empty)
             (filter #(or (not= (second discriminator) :row)
                          (not= (first discriminator) %)))
             (map (partial map (fn [[a b]]
                                 ; (println a b :col discriminator)
                                 (= (get-col m a)
                                    (get-col m b)))))
             (map-indexed vector)
             (filter (comp (partial every? identity)
                           second))
             (filter second)
             first
             (#(when %
                 [(inc (first %)) :col])))
        (->> rows
             (map #(for [r (range (min (inc %) (- (dec num-rows) %)))]
                     [(- % r) (inc (+ % r))]))
             (filter not-empty)
             (filter #(or (not= (second discriminator) :row)
                          (not= (first discriminator) %)))
             (map (partial map (fn [[a b]]
                                 ; (println a b :row discriminator)
                                 (=  (get-row m a)
                                     (get-row m b)))))
             (map-indexed vector)
             (filter (comp (partial every? identity)
                           second))
             (filter second)
             first
             (#(when %
                 [(inc (first %)) :row]))))))

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
     (map #(update % 1 (fn [x] (condp = x
                                 :col 1
                                 :row 100))))
     (map (partial apply *))
     (reduce +))

;; part 2
(->> sample-input
     (mapv #(let [num-rows (count %)
                  num-cols (count (first %))
                  m (gen-map %)
                  smudge (get-refl-value m num-rows num-cols)]
              (->>
               (for [r (range num-rows)
                     c (range num-cols)]
                 (let [m-prime (update m [r c] (fn [c] (if (= c \#) \. \#)))
                       res (get-refl-value m-prime num-rows num-cols smudge)]
                   res))
               (filter identity)
               first)))
     ; (map get-refl-value)
     ; (map #(update % 1 (fn [x] (condp = x
     ;                             :col 1
     ;                             :row 100))))
     ; (map (partial apply *))
     ; (reduce +)
     )

