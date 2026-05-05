-- =============================================
-- 1) borrow_records tablosunu oluştur
-- =============================================
CREATE TABLE borrow_records (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    student_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    book_id UUID NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    borrowed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    due_date TIMESTAMPTZ NOT NULL,
    returned_at TIMESTAMPTZ NULL
);

-- =============================================
-- 2) RLS'yi aktif et
-- =============================================
ALTER TABLE borrow_records ENABLE ROW LEVEL SECURITY;

-- =============================================
-- 3) Policiler
-- =============================================

-- Öğrenci sadece kendi kayıtlarını görebilir
CREATE POLICY "Öğrenci kendi kiralamalarını görebilir"
ON borrow_records
FOR SELECT
USING (auth.uid() = student_id);

-- Öğrenci yeni kiralama oluşturabilir
CREATE POLICY "Öğrenci kiralama oluşturabilir"
ON borrow_records
FOR INSERT
WITH CHECK (auth.uid() = student_id);

-- Öğrenci sadece kendi kaydını güncelleyebilir (iade için)
CREATE POLICY "Öğrenci kendi kaydını güncelleyebilir"
ON borrow_records
FOR UPDATE
USING (auth.uid() = student_id);

-- =============================================
-- 4) books tablosundaki available_copies'i
--    otomatik güncelleyen trigger
-- =============================================

-- Kiralama yapılınca available_copies azalt
CREATE OR REPLACE FUNCTION decrease_available_copies()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE books
    SET available_copies = available_copies - 1
    WHERE id = NEW.book_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_borrow_insert
AFTER INSERT ON borrow_records
FOR EACH ROW EXECUTE FUNCTION decrease_available_copies();

-- İade edilince available_copies artır
CREATE OR REPLACE FUNCTION increase_available_copies()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.returned_at IS NULL AND NEW.returned_at IS NOT NULL THEN
        UPDATE books
        SET available_copies = available_copies + 1
        WHERE id = NEW.book_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER on_borrow_return
AFTER UPDATE ON borrow_records
FOR EACH ROW EXECUTE FUNCTION increase_available_copies();

-- =============================================
-- 5) Aynı kitabı tekrar ödünç alamama kontrolü
-- =============================================
CREATE OR REPLACE FUNCTION check_active_borrow()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM borrow_records
        WHERE student_id = NEW.student_id
          AND book_id = NEW.book_id
          AND returned_at IS NULL
    ) THEN
        RAISE EXCEPTION 'Bu kitabı zaten ödünç almışsınız.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_duplicate_borrow
BEFORE INSERT ON borrow_records
FOR EACH ROW EXECUTE FUNCTION check_active_borrow();