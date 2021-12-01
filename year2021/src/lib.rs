pub mod io {
    pub fn read_input(day: i32) -> String {
        let input = std::fs::read_to_string(format!("src/res/day{}.txt", day)).unwrap();
        return String::from(input.trim_end());
    }
}
