/**
 * Intstructions inicate moving a waypoint around current ship location.
 * F now moves ship toward waypoint number of times indicated by value.
 * The waypoint starts 10 units east and 1 unit north relative to the ship.
 *
 * Note [SCRAPPED] indicates unfinshed method for non right angle waypoint transformations
 */

import { gimmeInput } from "../read";
const data = gimmeInput("12.txt");
if (data) {
  const intructions: string[] = data.split("\n");
  let x = 10,
    y = 1,
    d,
    shipX = 0,
    shipY = 0;
  // shipTheta;
  intructions.forEach((ins) => {
    const cmd = ins.slice(0, 1);
    const val = parseInt(ins.slice(1));
    // [SCRAPPED]: pathagorean theorem for L, R rotations:
    // d = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    // shipTheta = Math.atan(y / x); //radians
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
        // [SCRAPPED]: rotate relative coordinates by incrementing theta
        // and assigning new x,y from unchanged distance, d.
        // shipTheta += degToRad(val);
        // x = d * Math.cos(shipTheta);
        // y = d * Math.sin(shipTheta);
        [x, y] = turn90Deg(x, y, val, "L");
        break;
      }
      case "R": {
        [x, y] = turn90Deg(x, y, val, "R");
        break;
      }
      case "F": {
        // Move to waypoint x,y - val # of times
        shipX += val * x;
        shipY += val * y;
        break;
      }
      default: {
        //do nothing
        break;
      }
    }
  });
  const dist = manhattanDistance([0, 0], [shipX, shipY]);
  console.log(
    `Ship stopped @ [${shipX},${shipY}] facing degree ${Math.round(
      (180 * Math.atan(y / x)) / Math.PI
    )}, at distance ${Math.round(dist)} from start.`
  );
}

type Coordinate = [x: number, y: number];
function manhattanDistance(i: Coordinate, f: Coordinate): number {
  return Math.abs(f[0] - i[0]) + Math.abs(f[1] - i[1]);
}

/**
 * Converts 90 degree turns to changes in x, y
 * Mirrors coordinates according to direction of rotation and # of right angle turns.
 * (Simplified x,y transformations as doing trig with a carried theta every rotation was producing errors)
 */
function turn90Deg(
  x: number,
  y: number,
  deg: number,
  dir: string
): [x: number, y: number] {
  const turns = deg / 90;
  for (let i = 0; i < turns; i++) {
    if (dir === "L") {
      [x, y] = [-y, x];
    }
    if (dir === "R") {
      [x, y] = [y, -x];
    }
  }
  return [x, y];
}
