package com.mdb.connectdots;

public class ConnectDots implements Runnable {
    int SEARCHPLY = 3;
    int game_board[] = new int[64];
    int draw_board[] = new int[64];
    int searchmoves[] = new int[9];
    boolean won = false;
    int ply = 0;
    int player = -1;
    boolean players_move = true;

    public ConnectDots() {
        Restart();
    }
    public void Restart() {
        for (int x = 0; x < 64; x++) game_board[x] = draw_board[x] = 0;
        ply = 0;
        player = -1;
        players_move = true;
        won = false;
    }

    public void run() {
        search();
        make_move(searchmoves[0], 1);
        for (int xx = 0; xx < 64; xx++) draw_board[xx] = game_board[xx];
//drawboard
        players_move = true;
        if (win() != 0) {
            won = true;
            players_move = false;
        }
    }

    public void enterMove(int move) {
        if (players_move && game_board[move] == 0) {
            players_move = false;
            make_move(move, -1);
            for (int xx = 0; xx < 64; xx++) draw_board[xx] = game_board[xx];
//drawboard
            if (win() != 0) {
                ///   g.drawString("You Win! Click Restart!",25,265);
                players_move = false;
                return;
            }
            ply = 0;
            player = -1;
            //MainActivity.mainActivity.runOnUiThread(this);
        }
    }

    private void unmake_move(int move) {
        int y;
        for (y = 0; y < 7; ++y)
            if (game_board[(y << 3) + move] != 0) break;
        game_board[(y << 3) + move] = 0;
    }

    private void make_move(int move, int color) {
        int y;
        for (y = 0; y < 7; ++y)
            if (game_board[(y << 3) + move] != 0) break;
        game_board[((y - 1) << 3) + move] = color;
    }

    public String getBoard() {
        String board="[{'cols':[ROWS]},{'message':'XMSSAGE'}]";
        String rows ="";
        int x, y;
        for (x = 0; x < 7; ++x) {
            String csv="";
            String row = "{'rows':'CSV'},";
            for (y = 0; y < 7; ++y){
                    csv += game_board[(y << 3) + x]+",";
            }
            rows+=row.replace("CSV",csv.substring(0,csv.length()-1));
        }
        board=board.replace("ROWS",rows);
        if (win() != 0) {
            board=board.replace("XMSSAGE",win()<0?"You Win":"I Win");
        }
        if(players_move)
            board=board.replace("XMSSAGE","Enter Move");
        else
            board=board.replace("XMSSAGE","Thinking");

        return board;
    }

    private int win() {
        int x, y, c, x2, y2, ct = 0;

        for (x = 0; x < 7; ++x)
            if (game_board[x] != 0) ++ct;
        if (ct == 7) return 69; //tie
        for (y = 0; y < 7; ++y) {
            for (x = 0; x < 7; ++x) {
                if ((c = game_board[(x << 3) + y]) != 0) {
                    // horizontal win
                    for (ct = 0, y2 = y + 1; y2 < 7 && game_board[(x << 3) + y2] == c; ++y2, ++ct)
                        ;
                    if (ct == 3) {
                        return c;
                    }
                    // vertical win //
                    for (ct = 0, x2 = x + 1; x2 < 7 && game_board[(x2 << 3) + y] == c; ++x2, ++ct)
                        ;
                    if (ct == 3) {
                        return c;
                    }
                    // downward diagnal win //
                    for (ct = 0, x2 = x + 1, y2 = y + 1;
                         x2 < 7 && y2 < 7 && game_board[(x2 << 3) + y2] == c;
                         ++ct, ++x2, ++y2)
                        ;
                    if (ct == 3) {
                        return c;
                    }
                    // upward diagnal win //
                    for (ct = 0, x2 = x + 1, y2 = y - 1;
                         x2 < 7 && y2 >= 0 && game_board[(x2 << 3) + y2] == c;
                         ++ct, ++x2, --y2)
                        ;
                    if (ct == 3) {
                        return c;
                    }
                }
            }
        }
        return 0;
    }

    private int evaluate_position() {
        int x, y, c, x2, y2, ct, score = 0;

        for (y = 0; y < 7; ++y) {
            for (x = 0; x < 7; ++x) {
                if ((c = game_board[(x << 3) + y]) != 0) {
                    if (y == 0 || y == 7)
                        score -= 7;
                    // horizontal win //
                    for (ct = 0, y2 = y + 1; y2 < 7 && game_board[(x << 3) + y2] == c; ++y2, ++ct)
                        ;
                    if (ct == 3) score += (c << 9);
                    if (ct == 2) score += (c << 6);
                    if (ct == 1) score += (c << 5);

                    // vertical win //
                    for (ct = 0, x2 = x + 1; x2 < 7 && game_board[(x2 << 3) + y] == c; ++x2, ++ct)
                        ;
                    if (ct == 3) score += (c << 9);
                    if (ct == 2) score += (c << 6);
                    if (ct == 1) score += (c << 5);

                    // downward diagnal win //
                    for (ct = 0, x2 = x + 1, y2 = y + 1;
                         x2 < 7 && y2 < 7 && game_board[(x2 << 3) + y2] == c;
                         ++ct, ++x2, ++y2)
                        ;
                    if (ct == 3) score += (c << 9);
                    if (ct == 2) score += (c << 6);
                    if (ct == 1) score += (c << 5);

                    // upward diagnal win //
                    for (ct = 0, x2 = x + 1, y2 = y - 1;
                         x2 < 7 && y2 >= 0 && game_board[(x2 << 3) + y2] == c;
                         ++ct, ++x2, --y2)
                        ;
                    if (ct == 3) score += (c << 9);
                    if (ct == 2) score += (c << 6);
                    if (ct == 1) score += (c << 5);
                }
            }
        }
        return score + (int) Math.floor(Math.random() * 15);
    }

    private int search() {
        int ct, score, highscore, x;
        int generated_moves[];
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            ;
        }
        generated_moves = new int[9];

        ++ply;
        player *= -1;
        highscore = player * -32000;

        if (ply < SEARCHPLY) {
            generate_moves(generated_moves);
            for (ct = 0; ct < 7 && generated_moves[ct] != 99; ct++) {
                make_move(generated_moves[ct], player);

                if ((score = win()) != 0)
                    score = score * (10000 >> ply); //10000/ply
                else
                    score = search();

                if (player == 1) {
                    if (score > highscore) {
                        highscore = score;
                        searchmoves[ply - 1] = generated_moves[ct];
                    }
                } else {
                    if (score < highscore) {
                        highscore = score;
                        searchmoves[ply - 1] = generated_moves[ct];
                    }
                }
                unmake_move(generated_moves[ct]);
            }
        } else {

            --ply;
            player *= -1;
            return evaluate_position();
        }

        --ply;
        player *= -1;
        return highscore;
    }

    private void generate_moves(int moves[]) {
        int x, move_ct = 0;
        for (x = 0; x < 7; ++x)
            if (game_board[x] == 0)
                moves[move_ct++] = x;
        moves[move_ct] = 99; //magic number for end of list
    }
}
