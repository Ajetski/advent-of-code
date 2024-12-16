(ns day15
  (:require
   [core :as c]
   [input-manager :refer [get-input-raw]]
   [clojure.string :as str]))

;; parse input
(let [[a b] (c/split-on-double-newlines
             (get-input-raw 15))
      raw-grid (->> (str/split-lines a)
                    c/map-by-coords)]
  (def instructions (->> (filter #(not= % \newline) b)
                         (map c/arrow-char->dir)))
  (def start (->> (filter #(= (second %) \@) raw-grid)
                  ffirst))
  (def grid (assoc raw-grid start \.))

  (def grid-2 (->>  (str/replace a "O" ".")
                    str/split-lines
                    (mapv #(apply str (interleave % %)))
                    c/map-by-coords))

  (def boulders-2 (->> (filter #(= (second %) \O) raw-grid)
                       (map first)
                       (map #(update % 0 * 2))
                       set))

  (def walls-2 (->> (filter #(= (second %) \#) grid-2)
                    (map first)
                    set)))

(def step-fns {:up [0 dec]
               :down [0 inc]
               :right [1 inc]
               :left [1 dec]})
(defn step [loc dir]
  (apply update loc (step-fns dir)))

;; part 1
(->> instructions
     (reduce (fn [{:keys [grid loc] :as acc}
                  dir]
               (let [check (step loc dir)
                     gap (loop [p check]
                           (condp = (grid p)
                             nil nil
                             \# nil
                             \O (recur (step p dir))
                             \. p))] ; hey look, some space!
                 (cond
                   (nil? gap) acc

                   (= gap check) (assoc acc :loc check)

                   :else {:loc check
                          :grid (-> grid
                                    (assoc check \.)
                                    (assoc gap \O))})))
             {:grid grid, :loc start})
     :grid
     (filter #(= (second %) \O))
     (map first)
     (map #(+ (second %)
              (* 100 (first %))))
     (reduce +))
