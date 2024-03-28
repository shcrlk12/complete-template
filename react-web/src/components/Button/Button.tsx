import { StyledButton } from "./Button.styled";

type ButtonProps = {
  className?: string;
  isPrimary?: boolean;
  isDangerous?: boolean;
  fontWeight?: number;
  text?: string;
  onClick?: any;
  width?: string;
};

const Button = ({ className, isPrimary, isDangerous, fontWeight, text, width = "auto", onClick }: ButtonProps) => {
  return (
    <>
      <StyledButton
        className={className}
        isPrimary={isPrimary}
        isDangerous={isDangerous}
        fontWeight={fontWeight}
        width={width}
        onClick={onClick}>
        {text}
      </StyledButton>
    </>
  );
};

export default Button;
