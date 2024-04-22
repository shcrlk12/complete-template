import { Body } from "@pages/Availability/Availability.styled";
import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";

const GlobalStyles = createGlobalStyle` 
  ${reset} // styled-reset이라는 패키지를 설치해야한다. 몇가지만 reset해 줄 경우 사용하지 않아도 무방하다.

  html, body, div, span, applet, object, iframe,
h1, h2, h3, h4, h5, h6, p, blockquote, pre,
a, abbr, acronym, address, big, cite, code,
del, dfn, em, img, ins, kbd, q, s, samp,
small, strike, strong, sub, sup, tt, var,
b, u, i, center,
dl, dt, dd, ol, ul, li,
fieldset, form, label, legend,
table, caption, tbody, tfoot, thead, tr, th, td,
article, aside, canvas, details, embed, 
figure, figcaption, footer, header, hgroup, 
menu, nav, output, ruby, section, summary,
time, mark, audio, video {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure, 
footer, header, hgroup, menu, nav, section {
	display: block;
}
body {
	line-height: 1;
}
ol, ul {
	list-style: none;
}
blockquote, q {
	quotes: none;
}
blockquote:before, blockquote:after,
q:before, q:after {
	content: '';
	content: none;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
}
input {
	outline: none;
}
a{
	text-decoration: none;
	color: ${({ theme }) => theme.colors.black};
}

*{
    box-sizing: border-box;
}

html,
body {
    width: 100%;
    height: 100%;
	background-color: ${({ theme }) => theme.colors.bg};

}

#root{
    width: 100%;
    height: 100%;
}

.selected-cell{
	animation-iteration-count: infinite;
	animation-duration: 150ms;
  	animation-name: selected;
	opacity: 0.7;
}

@keyframes selected {
  0% {
	outline: ${({ theme }) => theme.colors.black} dashed 1px;

  }

  100% {
	outline: ${({ theme }) => theme.colors.black} dotted 1px;
  }
}
`;

export default GlobalStyles;
