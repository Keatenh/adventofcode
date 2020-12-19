/**
 * Stricter field validation:
 * -----------------------------
 * [x] byr (Birth Year) - four digits; at least 1920 and at most 2002.
 * [x] iyr (Issue Year) - four digits; at least 2010 and at most 2020.
 * [x] eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
 * [x] hgt (Height) - a number followed by either cm or in:
 * [x] If cm, the number must be at least 150 and at most 193.
 * [x] If in, the number must be at least 59 and at most 76.
 * [x] hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
 * [x] ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
 * [x] pid (Passport ID) - a nine-digit number, including leading zeroes.
 * cid (Country ID) - ignored, missing or not.
 */

import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("4.txt");
if (data) {
  // Get entries
  const arr = data.split("\n\n");
  // Convert to array of objects
  const objArr = arr.map((entry) => {
    return entry
      .split(/\s+/g)
      .map((x) => x.split(":").map((y) => y.trim()))
      .reduce((acc: any, cur) => {
        acc[cur[0]] = cur[1];
        return acc;
      }, {});
  });

  function validateKeys(obj: any) {
    const hasProps = (prop: string) => obj.hasOwnProperty(prop);
    return ["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"].every(hasProps);
  }
  const newArr = objArr
    .filter((obj) => {
      return validateKeys(obj);
    })
    .filter((obj) => {
      const n = parseInt(obj.byr);
      return obj.byr.length === 4 && n >= 1920 && n <= 2002;
    })
    .filter((obj) => {
      const n = parseInt(obj.iyr);
      return obj.iyr.length === 4 && n >= 2010 && n <= 2020;
    })
    .filter((obj) => {
      const n = parseInt(obj.eyr);
      return obj.eyr.length === 4 && n >= 2020 && n <= 2030;
    })
    .filter((obj) => {
      const unit = obj.hgt.slice(-2);
      const numStr = obj.hgt.substring(0, obj.hgt.length - 2);
      const num = parseInt(numStr);
      return (
        !isNaN(Number(numStr)) &&
        ((unit === "cm" && num >= 150 && num <= 193) ||
          (obj.hgt.slice(-2) === "in" && num >= 59 && num <= 76))
      );
    })
    .filter((obj) => {
      return obj.hcl.match(/^#[0-9a-f]{6}$/);
    })
    .filter((obj) => {
      return ["amb", "blu", "brn", "gry", "grn", "hzl", "oth"].includes(
        obj.ecl
      );
    })
    .filter((obj) => {
      return obj.pid.match(/^[0-9]{9}$/);
    });
  console.log(`Found ${newArr.length} valid passports, out of ${objArr.length}`);
}
