import fs from "fs";
import path from "path";

export function gimmeInput(file: string): string | undefined {
  try {
    const data = fs.readFileSync(path.join(__dirname, `../input/${file}`), "utf8");
    return data;
  } catch (err) {
    console.error(err);
  }
}
