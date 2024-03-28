import "styled-components";
import { ColorsTypes, FontSizeTypes, SizeTypes } from "./Theme";

declare module "styled-components" {
  export interface DefaultTheme {
    colors: ColorsTypes;
    fontSize: FontSizeTypes;
    size: SizeTypes;
  }
}
