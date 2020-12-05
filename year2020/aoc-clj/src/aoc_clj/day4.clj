(ns aoc-clj.day4
 (:require
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string]))

(defonce input
  (->> "day4.txt"
       io/resource
       slurp
       string/split-lines))

;; -----------------------------------------------------------------------------

(def valid
  {"byr" #(<= 1920 (read-string %) 2002)
   "iyr" #(<= 2010 (read-string %) 2020)
   "eyr" #(<= 2020 (read-string %) 2030)
   "hgt" (fn [s]
           (let [[_ hgt unit] (re-find #"^(\d+)(cm|in)$" s)]
             (case unit
               "cm" (<= 150 (read-string hgt) 193)
               "in" (<= 59 (read-string hgt) 76)
               false)))
   "hcl" (partial re-find #"^#[0-9a-f]{6}$")
   "ecl" #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
   "pid" (partial re-find #"^[0-9]{9}$")})

(defn parse [lines]
  (->> (partition-by empty? lines)
       (filter ffirst)
       (map #(apply hash-map (string/split (string/join #" " %) #"[: ]")))))

;; -----------------------------------------------------------------------------

(defn p1 [lines]
  (let [valid? (some-fn empty? (partial = #{"cid"}))
        f #(valid? (set/difference (set (keys valid)) (set (keys %))))]
    (count (filter f (parse lines)))))

(defn p2 [lines]
  (->> (parse lines)
       (filter #(>= (count %) (count valid)))
       (filter (fn [m] (every? (fn [k] (when (m k) ((valid k) (m k)))) (keys valid))))
       count))

(comment
  (= 213 (p1 input))
  (= 147 (p2 input)))

