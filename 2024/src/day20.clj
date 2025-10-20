(ns day20
  (:require
   input-manager))

(def maze (input-manager/get-input 2024 20))
(def grid (->> maze
               (map-indexed (fn [row-idx row]
                              (map-indexed (fn [col-idx c]
                                             [[row-idx col-idx] c])
                                           row)))
               (mapcat identity)
               (into {})))
(defn find-char [c]
  (->> grid
       (filter (comp #{c} second))
       ffirst))
(def start (find-char \S))
(def end (find-char \E))

(def direction-offsets #{[0 1] [1 0] [0 -1] [-1 0]})

(defn find-search-path []
  (loop [[[pos steps] & rst] [[start 0]]
         visited #{start}
         shortest {start 0}]
    (if (= pos end)
      shortest
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

