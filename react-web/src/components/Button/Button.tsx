import { StyledButton } from "./Button.styled";

type ButtonProps = {
  className?: string;
  isPrimary?: boolean;
  isDangerous?: boolean;
  fontWeight?: number;
  text?: string | JSX.Element;
  onClick?: any;
  width?: string;
  height?: string;
  radius?: string;
  padding?: string;
  disabled?: boolean;
  type?: "submit" | "button" | "reset";
};

const Button = ({
  className,
  isPrimary,
  isDangerous,
  fontWeight,
  text,
  width = "auto",
  height = "40px",
  radius = "20px",
  padding = "5px 20px",
  onClick,
  disabled = false,
  type,
}: ButtonProps) => {
  return (
    <>
      <StyledButton
        type={type}
        className={className}
        isPrimary={isPrimary}
        isDangerous={isDangerous}
        fontWeight={fontWeight}
        width={width}
        height={height}
        radius={radius}
        padding={padding}
        onClick={onClick}
        disabled={disabled}>
        {text}
      </StyledButton>
    </>
  );
};

export default Button;
