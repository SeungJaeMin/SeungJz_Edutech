import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

const AuthCallbackPage = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { login } = useAuth();

  useEffect(() => {
    const token = searchParams.get('token');

    if (token) {
      login(token);
      navigate('/');
    } else {
      console.error('No token found in callback');
      navigate('/login');
    }
  }, [searchParams, login, navigate]);

  return (
    <div style={{
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '100vh'
    }}>
      <div>
        <h2>로그인 중...</h2>
        <p>잠시만 기다려주세요.</p>
      </div>
    </div>
  );
};

export default AuthCallbackPage;
