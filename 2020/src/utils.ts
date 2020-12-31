export function deepCopy(data: any): any {
  return JSON.parse(JSON.stringify(data));
}
