(ns day14
  (:require
   [clojure.string :as str]
   [core :refer [get-puzzle-input]]))

(comment

  (def input (reverse (str/split-lines "O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....")))

  (def input (reverse (get-puzzle-input 14))))

(defn sorting-fn [& args]
  (apply compare (map (juxt #(* -1 (first %))
                            #(second %))
                      args)))

(defn gen-map [input]
  (->>  input
        (map-indexed (fn [row line]
                       (map-indexed (fn [col c]
                                      [[(inc row) (inc col)] c])
                                    line)))
        (apply concat)
        (into (sorted-map-by sorting-fn))))

(def empty-space-chars #{\. \O})

(defn get-next-pos [[r c] dir]
  (condp = dir
    :north [(inc r) c]
    :east [r (inc c)]
    :south [(dec r) c]
    :west [r (dec c)]))

(defn next-os [m os dir]
  (->> os
       (map (fn [pos]
              (let [next-pos (get-next-pos pos dir)
                    ch (m next-pos)]
                (if (and ch
                         (empty-space-chars ch)
                         (not (os next-pos)))
                  next-pos
                  pos))))
       (into (sorted-set-by sorting-fn))))

(defn print-board [m os]
  (println)
  (doseq [line (->> m
                    (group-by ffirst)
                    (sort-by first)
                    (reverse)
                    (map second)
                    (map (partial map #(cond (os (first %)) \O
                                             (= (second %) \O) \.
                                             :else (second %))))
                    (map #(str/join "" %)))]
    (println line)))

(->> (let [m (gen-map input)]
       (loop [os (->> m
                      (filter #(= (second %) \O))
                      (map first)
                      (into (sorted-set-by sorting-fn)))
              dirs (take (* 1000 4) (cycle [:north :west :south :east]))
              dbg 1]
         (if (not-empty dirs)
           (let [os' (next-os m os (first dirs))]
             (if (= os os')

               (recur os (rest dirs) (if (= (first dirs) :east)
                                       (do (println dbg (reduce + (map first os)))
                                           (inc dbg))
                                       dbg))
               (recur os' dirs dbg)))
           os)))
     (map first)
     (reduce +))

;; pushing garbage code. use prints to uncover cycles. use basic math to find p2 solution

