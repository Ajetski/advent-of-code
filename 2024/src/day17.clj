(ns day17
  (:require input-manager
            [core :as c]
            [clojure.string :as str]))

(let [[registers _ [program]]
      (->> #_(input-manager/get-input 2024 17)
           "Register A: 729\nRegister B: 0\nRegister C: 0\n\nProgram: 0,1,5,4,3,0"
           (str/split-lines)
           (partition-by (partial = "")))]
  (def registers (update-vals
                   (into {}
                         (map (comp vec
                                    rest
                                    first
                                    (partial re-seq #"(\w)+: (.*)")) registers))
                   parse-long))
  (def program (mapv parse-long
                     (-> program
                         (str/split #" ")
                         second
                         (str/split #",")))))

(defn combo-op->value [combo-op registers]
  (condp = combo-op
    4 (registers "A")
    5 (registers "B")
    6 (registers "C")
    7 (throw (ex-message "invalid"))
    combo-op))

(defn compute
  ([registers program]
   (compute registers program 0 []))
  ([{A "A", B "B", C "C" :as registers} program instr-ptr out]
   (if (>= instr-ptr (count program))
     [out registers]
     (let [operation (program instr-ptr)
           operand (program (inc instr-ptr))
           combo-operand (delay (combo-op->value operand registers))
           instr-ptr' (+ 2 instr-ptr)]
       (condp = operation
         0 (recur (->> (int (/ A (max 1 (bit-shift-left 2 (dec @combo-operand)))))
                       (assoc registers "A"))
                  program instr-ptr' out)
         1 (recur (->> operand
                       (bit-xor B)
                       (assoc registers "B"))
                  program instr-ptr' out)
         2 (recur (->> (mod @combo-operand 8)
                       (assoc registers "B"))
                  program instr-ptr' out)
         3 (if (= A 0)
             (recur registers program instr-ptr' out)
             (recur registers program operand out))
         4 (recur (->> (bit-xor B C)
                       (assoc registers "B"))
                  program instr-ptr' out)
         5 (recur registers
                  program
                  instr-ptr'
                  (conj out (mod @combo-operand 8)))
         6 (recur (->> (int (/ A (max 1 (bit-shift-left 2 (dec @combo-operand)))))
                       (assoc registers "B"))
                  program instr-ptr' out)
         7 (recur (->> (int (/ A (max 1 (bit-shift-left 2 (dec @combo-operand)))))
                       (assoc registers "C"))
                  program instr-ptr' out))))))

;; part 1
(->> (compute registers program)
     first
     (interpose ",")
     (apply str))

;; part 2
(loop [n 0]
  (when (= (mod n 10000) 0)
    (println n))
  (let [res (try (compute (merge registers {"A" n}) program)
                 (catch Exception _e
                   nil))]
    (if (= (first res) program)
      n
      (recur (inc n)))))

(comment
  registers
  program

  (compute {"C" 1} [2 6])
  (compute {"A" 10} [5, 0, 5, 1, 5, 4])
  (compute {"A" 2024} [0, 1, 5, 4, 3, 0])
  (compute {"B" 29} [1, 7])
  (compute {"B" 2024, "C" 43690} [4 0])
  )
