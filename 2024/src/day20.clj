(ns day20
  (:require
   [core :as c]
   input-manager))

(def maze (input-manager/get-input 2024 20)
  #_(c/split-whitespace "###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############"))
(def grid (->> maze
               (map-indexed (fn [row-idx row]
                              (map-indexed (fn [col-idx c]
                                             [[row-idx col-idx] c])
                                           row)))
               (mapcat identity)
               (into {})))
(def start (->> grid
                (filter (comp #{\S} second))
                ffirst))
(def end (->> grid
              (filter (comp #{\E} second))
              ffirst))

(def direction-offsets #{[0 1] [1 0] [0 -1] [-1 0]})

(defn find-search-path []
  (loop [[[pos steps] & rst] [[start 0]]
         visited #{start}
         shortest {start 0}]
    (cond
      (= pos end) shortest

      :else
      (let [searches (->> direction-offsets
                          (map (partial mapv + pos))
                          (filter grid)
                          (filter (comp not visited))
                          (filter (comp not #{\#} grid)))
            searches-w-path-lengths (mapv #(vector % (inc steps)) searches)]
        (recur (into rst searches-w-path-lengths)
               (into visited searches)
               (into shortest searches-w-path-lengths))))))

(defn find-best-cheat []
  (let [search-path (find-search-path)
        ; no-cheat-solution (search-path end)
        walls (->> grid
                   (filter (comp #{\#} second))
                   (map first))
        cheats (->> walls
                    (map (fn [pos]
                           (->> direction-offsets
                                (map (partial mapv + pos))
                                (map search-path)
                                (filter identity))))
                    (filter not-empty)
                    (filter (comp (partial <= 2) count))
                    (map sort)
                    (mapcat (fn [[a & bs]]
                              (map #(- % a) bs))))]
    (frequencies cheats)))

(->> (find-best-cheat)
     (filter (comp #(> % 100) first))
     (map second)
     (apply +))

