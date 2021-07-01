import { Pipe, PipeTransform } from "@angular/core";

export function sortBy(array: any[], by: string, order?: string) : any[] {
  const sortOrder = order ? order : 'asc';
  const mul = sortOrder == "desc" ? 1 : -1;
  return array.sort((a, b) => a[by] > b[by] ? 1*mul : a[by] < b[by] ? -1*mul : 0);
}

@Pipe({
  name: "orderBy"
})
export class OrderByPipe implements PipeTransform {
  transform(array: any[], by: string, order?: string): any[] {
    return sortBy(array, by, order);
  }
}
