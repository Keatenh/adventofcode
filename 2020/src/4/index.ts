/**
 * Check for valid fields in text file
 * byr (Birth Year)
 * iyr (Issue Year)
 * eyr (Expiration Year)
 * hgt (Height)
 * hcl (Hair Color)
 * ecl (Eye Color)
 * pid (Passport ID)
 * cid (Country ID) [OPTIONAL]
 */
import { gimmeInput } from "../read";

// Read file
const data = gimmeInput("4.txt");
let validCount = 0;
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
  // Check created objects for required properties:
  function validate(obj: any) {
    const hasProps = (prop: string) => obj.hasOwnProperty(prop);
    return ["byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"].every(hasProps);
  }
  objArr.forEach((e) => {
    if (validate(e)) {
      validCount++;
    }
  });
  console.log(`Found ${validCount} valid passports, out of ${objArr.length}`);
}

// Check each obj for required fields and count
