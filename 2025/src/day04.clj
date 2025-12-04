(ns day04
  (:require input-manager
            core))

(def input (input-manager/get-input 2025 4))

(def grid (core/map-by-coords input))

(def offsets [[-1 -1] [-1 0] [-1 1]
              [0 -1]         [0 1]
              [1 -1]  [1 0]  [1 1]])

;; part 1
(->> grid
     (filter (fn [[_ v]]
               (= v \@)))
     (map first)
     (filter (fn [pos]
               (< (count (filter (comp (partial = \@) grid)
                                 (for [o offsets]
                                   (mapv + pos o))))
                  4)))
     count)

;; part 2
(loop [grid grid
       acc 0]
  (let [locs
        (->> grid
             (filter (fn [[_ v]]
                       (= v \@)))
             (map first)
             (filter (fn [pos]
                       (< (count (filter (comp (partial = \@) grid)
                                         (for [o offsets]
                                           (mapv + pos o))))
                          4))))]
    (if (empty? locs)
      acc
      (recur (reduce #(dissoc %1 %2) grid locs)
             (+ acc (count locs))))))
