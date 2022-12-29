type Board = Vec<[bool; 7]>;
#[derive(Clone, Copy, Debug)]
enum BlockType {
    Square,
    L,
    Horizontal,
    Vertical,
    Cross,
}
impl BlockType {
    fn height(&self) -> usize {
        match *self {
            Square => 2,
            L => 3,
            Horizontal => 1,
            Vertical => 4,
            Cross => 3,
        }
    }
}

use BlockType::*;

#[derive(Debug)]
struct Position {
    row: usize,
    col: usize,
}

#[derive(Debug)]
struct Block {
    pos: Position,
    block_type: BlockType,
}
impl Block {
    fn left(&mut self, board: &Board) {
        let cond = match self.block_type {
            Square => {
                self.pos.col > 0
                    && !board[self.pos.row][self.pos.col - 1]
                    && !board[self.pos.row - 1][self.pos.col - 1]
            }
            L => {
                self.pos.col > 0
                    && !board[self.pos.row][self.pos.col + 1]
                    && !board[self.pos.row - 1][self.pos.col + 1]
                    && !board[self.pos.row - 2][self.pos.col - 1]
            }
            Cross => {
                self.pos.col > 0
                    && !board[self.pos.row][self.pos.col]
                    && !board[self.pos.row - 1][self.pos.col - 1]
                    && !board[self.pos.row - 2][self.pos.col]
            }
            Horizontal => self.pos.col > 0 && !board[self.pos.row][self.pos.col - 1],
            Vertical => {
                self.pos.col > 0
                    && !board[self.pos.row][self.pos.col - 1]
                    && !board[self.pos.row - 1][self.pos.col - 1]
                    && !board[self.pos.row - 2][self.pos.col - 1]
                    && !board[self.pos.row - 3][self.pos.col - 1]
            }
        };
        if cond {
            self.pos.col -= 1;
        }
        println!("left to {:?}, {:?}", self.pos, self.block_type);
    }

    fn right(&mut self, board: &Board) {
        let cond = match self.block_type {
            Square => {
                self.pos.col < 5
                    && !board[self.pos.row][self.pos.col + 2]
                    && !board[self.pos.row - 1][self.pos.col + 2]
            }
            L => {
                self.pos.col < 4
                    && !board[self.pos.row][self.pos.col + 3]
                    && !board[self.pos.row - 1][self.pos.col + 3]
                    && !board[self.pos.row - 2][self.pos.col + 3]
            }
            Cross => {
                self.pos.col < 4
                    && !board[self.pos.row][self.pos.col + 2]
                    && !board[self.pos.row - 1][self.pos.col + 3]
                    && !board[self.pos.row - 2][self.pos.col + 2]
            }
            Horizontal => self.pos.col < 3 && !board[self.pos.row][self.pos.col + 4],
            Vertical => {
                self.pos.col < 6
                    && !board[self.pos.row][self.pos.col + 1]
                    && !board[self.pos.row - 1][self.pos.col + 1]
                    && !board[self.pos.row - 2][self.pos.col + 1]
                    && !board[self.pos.row - 3][self.pos.col + 1]
            }
        };
        if cond {
            self.pos.col += 1;
        }
        println!("right to {:?}, {:?}", self.pos, self.block_type);
    }

    /// Returns if the block comes to rest (and can't move down)
    /// board will only be mutated to insert the object if the block is at rest
    fn down(&mut self, board: &mut Board) -> bool {
        let is_falling = match self.block_type {
            Square => {
                self.pos.row > 1
                    && !board[self.pos.row - 2][self.pos.col]
                    && !board[self.pos.row - 2][self.pos.col + 1]
            }
            L => {
                self.pos.row > 2
                    && !board[self.pos.row - 3][self.pos.col]
                    && !board[self.pos.row - 3][self.pos.col + 1]
                    && !board[self.pos.row - 3][self.pos.col + 2]
            }
            Cross => {
                self.pos.row > 2
                    && !board[self.pos.row - 2][self.pos.col]
                    && !board[self.pos.row - 3][self.pos.col + 1]
                    && !board[self.pos.row - 2][self.pos.col + 2]
            }
            Horizontal => {
                self.pos.row > 0
                    && !board[self.pos.row - 1][self.pos.col]
                    && !board[self.pos.row - 1][self.pos.col + 1]
                    && !board[self.pos.row - 1][self.pos.col + 2]
                    && !board[self.pos.row - 1][self.pos.col + 3]
            }
            Vertical => self.pos.row > 3 && !board[self.pos.row - 4][self.pos.col],
        };
        if is_falling {
            self.pos.row -= 1;
        }

        println!(
            "down to {:?}, is falling? {:?}, {:?}",
            self.pos, is_falling, self.block_type
        );
        !is_falling
    }
    fn draw(&self, board: &mut Board) {
        match self.block_type {
            Square => {
                board[self.pos.row][self.pos.col] = true;
                board[self.pos.row][self.pos.col + 1] = true;
                board[self.pos.row - 1][self.pos.col] = true;
                board[self.pos.row - 1][self.pos.col + 1] = true;
            }
            L => {
                board[self.pos.row][self.pos.col + 2] = true;
                board[self.pos.row - 1][self.pos.col + 2] = true;
                board[self.pos.row - 2][self.pos.col + 2] = true;
                board[self.pos.row - 2][self.pos.col + 1] = true;
                board[self.pos.row - 2][self.pos.col] = true;
            }
            Cross => {
                board[self.pos.row][self.pos.col + 1] = true;
                board[self.pos.row - 1][self.pos.col + 1] = true;
                board[self.pos.row - 2][self.pos.col + 1] = true;
                board[self.pos.row - 1][self.pos.col] = true;
                board[self.pos.row - 1][self.pos.col + 2] = true;
            }
            Horizontal => {
                board[self.pos.row][self.pos.col] = true;
                board[self.pos.row][self.pos.col + 1] = true;
                board[self.pos.row][self.pos.col + 2] = true;
                board[self.pos.row][self.pos.col + 3] = true;
            }
            Vertical => {
                board[self.pos.row][self.pos.col] = true;
                board[self.pos.row - 1][self.pos.col] = true;
                board[self.pos.row - 2][self.pos.col] = true;
                board[self.pos.row - 3][self.pos.col] = true;
            }
        }
    }
}

type Data = &'static str;
type Output = i32;
fn parse(input: &'static str) -> Data {
    input
}
fn part_one(data: Data) -> Output {
    let mut blocks = vec![Horizontal, Cross, L, Vertical, Square]
        .into_iter()
        .cycle();
    let mut top = -1i32;
    let block_type = blocks.next().unwrap();
    let mut curr = Block {
        pos: Position { row: 3, col: 2 },
        block_type,
    };
    let mut board = vec![[false; 7]; 5];
    let mut num_rocks = 0;
    for c in data.chars().cycle() {
        match c {
            '>' => curr.right(&board),
            '<' => curr.left(&board),
            _ => {}
        }
        if curr.down(&mut board) {
            curr.draw(&mut board);
            top = board
                .iter()
                .enumerate()
                .rev()
                .find(|(_, el)| **el != [false; 7])
                .map(|(idx, _)| idx as i32)
                .unwrap();
            num_rocks += 1;

            println!();
            for line in board.iter().rev() {
                print!("|");
                line.iter()
                    .map(|filled| if *filled { '#' } else { '.' })
                    .for_each(|c| print!("{c}"));
                println!("|");
            }
            println!("+-------+");
            println!("{:?}", top);
            println!();

            if num_rocks == 15 {
                return top;
            }
            let block_type = blocks.next().unwrap();
            let start_row = top + 3 + block_type.height() as i32;
            while board.len() <= start_row as usize {
                board.push([false; 7]);
            }
            curr = Block {
                pos: Position {
                    row: start_row as usize,
                    col: 2,
                },
                block_type,
            };
        }
    }
    unreachable!()
}
fn part_two(_data: Data) -> Output {
    todo!()
}

advent_of_code_macro::generate_tests!(
    day 17,
    parse,
    part_one,
    part_two,
    sample tests [3068, 0],
    star tests [0, 0]
);
