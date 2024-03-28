import React from "react";
import { IoIosSearch } from "react-icons/io";
import { StyledSearchInput } from "./Input.styled";

type SearchInputProps = {
  height: number;
  text: string;
};

const SearchInput = ({ height = 40, text }: SearchInputProps) => {
  return (
    <StyledSearchInput height={height} type="search">
      <IoIosSearch className="searchIcon" size={(height / 3) * 2} />
      <input type="text" placeholder={text} />
    </StyledSearchInput>
  );
};

export default SearchInput;
