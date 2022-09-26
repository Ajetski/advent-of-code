use std::{collections::HashSet, fmt::Display};

#[derive(Clone, Debug, Eq)]
struct Tile {
    id: u128,
    grid: Vec<Vec<char>>,
}
impl Tile {
    fn rotate(&mut self) {
        let n = self.grid.len();

        // reverse rows
        for row in &mut self.grid {
            row.reverse();
        }

        // perform matrix transpose
        for i in 0..n {
            for j in i..n {
                let temp = self.grid[i][j];
                self.grid[i][j] = self.grid[j][i];
                self.grid[j][i] = temp;
            }
        }
    }

    fn flip(&mut self) {
        for row in &mut self.grid {
            row.reverse();
        }
    }

    fn fits_with_right_border_of(&self, left_tile: &Self) -> bool {
        let n = self.grid[0].len();
        for i in 0..n {
            if self.grid[i][0] != left_tile.grid[i][n - 1] {
                return false;
            }
        }
        true
    }

    fn fits_with_bottom_border_of(&self, above_tile: &Self) -> bool {
        let n = self.grid[0].len();
        for i in 0..n {
            if self.grid[0][i] != above_tile.grid[n - 1][i] {
                return false;
            }
        }
        true
    }
}

fn configure_board(
    board: &mut Vec<Vec<Option<Tile>>>,
    tiles_left: &mut HashSet<Tile>,
    x: usize,
    y: usize,
    n: usize,
) -> Option<Vec<Vec<Tile>>> {
    if x == n {
        return Some(
            board
                .iter()
                .map(|row| {
                    row.iter()
                        .map(|cell| cell.as_ref().unwrap().clone())
                        .collect()
                })
                .collect(),
        );
    }

    let new_y = if y + 1 == n { 0 } else { y + 1 };
    let new_x = if new_y == 0 { x + 1 } else { x };
    for mut tile in tiles_left.clone() {
        tiles_left.remove(&tile);
        for _ in 0..4 {
            for _ in 0..2 {
                if (x == 0 && y == 0)
                    || (x != 0
                        && tile.fits_with_bottom_border_of(board[x - 1][y].as_ref().unwrap()))
                    || (y != 0 && tile.fits_with_right_border_of(board[x][y - 1].as_ref().unwrap()))
                {
                    board[x][y] = Some(tile.clone());

                    if let Some(ans) = configure_board(board, tiles_left, new_x, new_y, n) {
                        return Some(ans);
                    }
                }
                tile.flip();
            }
            tile.rotate();
        }
        tiles_left.insert(tile);
    }

    None
}
fn part_one(mut input: HashSet<Tile>) -> u128 {
    let n2 = input.len();
    let n = (n2 as f64).sqrt() as usize;
    let mut board = vec![vec![None; n]; n];
    let board = configure_board(&mut board, &mut input, 0, 0, n).unwrap();
    board[0][0].id * board[0][n - 1].id * board[n - 1][0].id * board[n - 1][n - 1].id
}

fn part_two(mut input: HashSet<Tile>) -> u128 {
    let n2 = input.len();
    let n = (n2 as f64).sqrt() as usize;
    let mut board = vec![vec![None; n]; n];
    let board = configure_board(&mut board, &mut input, 0, 0, n).unwrap();

    let mut pic = vec![vec![]; n];
    let tile_size = board[0][0].grid.len();
    for (x, board_row) in board.iter().enumerate() {
        for tile in board_row {
            for (i, tile_row) in tile.grid.iter().enumerate() {
                for c in tile_row {
                    pic[x * (tile_size - 2) + i].push(*c);
                }
            }
        }
    }

    // flip/rotate pitctures
    // traverse each image for sea monsters

    todo!()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn day20_part_one_sample() {
        let input = parse_inputs(include_str!("../inputs/day20_sample.txt"));
        assert_eq!(part_one(input), 20899048083289);
    }

    #[test]
    fn day20_part_one() {
        let input = parse_inputs(include_str!("../inputs/day20.txt"));
        assert_eq!(part_one(input), 66020135789767);
    }

    #[test]
    fn day20_part_two_sample() {
        let _input = parse_inputs(include_str!("../inputs/day20_sample.txt"));
        // assert_eq!(part_two(input), 273);
    }

    fn parse_inputs(input: &str) -> HashSet<Tile> {
        input
            .trim()
            .split("\n\n")
            .map(str::parse)
            .map(Result::unwrap)
            .collect()
    }
}

// utility traits for parsing, hashing, equality, and printing tiles
impl PartialEq for Tile {
    fn eq(&self, other: &Self) -> bool {
        self.id == other.id
    }
}
impl std::hash::Hash for Tile {
    fn hash<H: std::hash::Hasher>(&self, state: &mut H) {
        self.id.hash(state);
    }
}
impl std::str::FromStr for Tile {
    type Err = std::string::ParseError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (id_line, grid_lines) = s.split_once('\n').unwrap();
        Ok(Tile {
            id: id_line[5..id_line.chars().count() - 1].parse().unwrap(),
            grid: grid_lines
                .trim()
                .split('\n')
                .map(str::chars)
                .map(Iterator::collect)
                .collect(),
        })
    }
}
impl Display for Tile {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        writeln!(f, "tile: {}", self.id)?;
        let n = self.grid.len();
        for i in 0..n {
            writeln!(f, "{}", self.grid[i].iter().collect::<String>())?;
        }
        writeln!(f)?;
        Ok(())
    }
}
