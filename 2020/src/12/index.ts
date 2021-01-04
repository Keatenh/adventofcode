/**
 * Read instructions to navigate ship along cardinal directions, as well as along bearing.
 * After last instruction, determine Manhattan distance from starting point.
 * Start facing East.
 */
import { gimmeInput } from "../read";
const data = gimmeInput("12.txt");
if (data) {
  const intructions: string[] = data.split("\n");
  let theta = 0,
    x = 0,
    y = 0;
  intructions.forEach((ins) => {
    const cmd = ins.slice(0, 1);
    const val = parseInt(ins.slice(1));
    switch (cmd) {
      case "N": {
        y += val;
        break;
      }
      case "S": {
        y -= val;
        break;
      }
      case "E": {
        x += val;
        break;
      }
      case "W": {
        x -= val;
        break;
      }
      case "L": {
        theta += val;
        break;
      }
      case "R": {
        theta -= val;
        break;
      }
      case "F": {
        x += val * Math.cos(degToRad(theta));
        y += val * Math.sin(degToRad(theta));
        break;
      }
      default: {
        //do nothing
        break;
      }
    }
  });
  const dist = manhattanDistance([0, 0], [x, y]);
  console.log(
    `Ship stopped @ [${x},${y}] facing degree ${theta}, at distance ${dist} from start.`
  );
}

type Coordinate = [x: number, y: number];
function manhattanDistance(i: Coordinate, f: Coordinate): number {
  return Math.abs(f[0] - i[0]) + Math.abs(f[1] - i[1]);
}

/**
 * Converts degrees to radians (angle)
 */
function degToRad(degrees: number): number {
  return (Math.PI * degrees) / 180;
}
