(ns day02)

(defn compute
  "takes in a tape & runs the program
  returns the state of tape when program halts"
  ([tape]
   (compute 0 tape))
  ([pos tape]
   (let [op (tape pos)]
     (if (= op 99)
       tape
       (let [arg1-slot (tape (inc pos))
             arg1-value (tape arg1-slot)
             arg2-slot (tape (+ pos 2))
             arg2-value (tape arg2-slot)
             return-slot (tape (+ pos 3))]
         (recur (+ pos 4)
                (assoc tape return-slot
                       ((condp = op
                          1 +
                          2 *)
                        arg1-value arg2-value))))))))

(comment
  ;; part 1
  (-> input
      (assoc 1 12)
      (assoc 2 2)
      compute
      (get 0))

  ;; part 2
  (->> (for [x (range 80)
             y (range 80)]
         (let [t (-> input
                     (assoc 1 x)
                     (assoc 2 y)
                     compute)]
           {:noun (t 1)
            :verb (t 2)
            :val (t 0)}))
       (filter #(=  (:val %) 19690720))
       (map (fn [{:keys [noun verb]}]
              (+ (* 100 noun) verb)))
       first)

;; set up input
  (let [raw-input "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,13,1,19,1,10,19,23,1,6,23,27,1,5,27,31,1,10,31,35,2,10,35,39,1,39,5,43,2,43,6,47,2,9,47,51,1,51,5,55,1,5,55,59,2,10,59,63,1,5,63,67,1,67,10,71,2,6,71,75,2,6,75,79,1,5,79,83,2,6,83,87,2,13,87,91,1,91,6,95,2,13,95,99,1,99,5,103,2,103,10,107,1,9,107,111,1,111,6,115,1,115,2,119,1,119,10,0,99,2,14,0,0"]
    (require '[clojure.string :as str])
    (def input (->> (str/split raw-input #",")
                    (mapv parse-long)))
    input))
