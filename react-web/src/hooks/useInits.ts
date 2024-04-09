import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";

function useInits() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  return { dispatch, navigate };
}

export default useInits;
