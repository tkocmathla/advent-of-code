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

(defn p1 [lines]
  (let [fields #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid" "cid"}]
    (->> (partition-by empty? lines)
         (filter ffirst)
         (map #(set (take-nth 2 (string/split (string/join #" " %) #"[: ]"))))
         (map (partial set/difference fields))
         (filter (some-fn empty? (partial = #{"cid"})))
         count)))

(defn p2 [lines]
  (let [valid {"byr" #(<= 1920 (read-string %) 2002)
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
               "pid" (partial re-find #"^[0-9]{9}$")}]
    (->> (partition-by empty? lines)
         (filter ffirst)
         (map #(apply hash-map (string/split (string/join #" " %) #"[: ]")))
         (filter #(>= (count %) (count valid)))
         (filter (fn [m] (every? (fn [k] (when (m k) ((valid k) (m k)))) (keys valid))))
         count)))

(comment
  (= 213 (p1 input))
  (= 147 (p2 input)))

